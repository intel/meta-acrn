#
# Copyright (c) 2020, Intel Corporation.
#
# SPDX-License-Identifier: GPL-2.0-only
#
# DESCRIPTION
# This implements the 'acrn-bootimg-efi' source plugin class for 'wic'
#
# AUTHORS
# Lee Chee Yang <chee.yang.lee (at] intel.com>
#

import logging
import os
import shutil

from wic import WicError
from wic.engine import get_custom_config
from wic.pluginbase import SourcePlugin
from wic.misc import (exec_cmd, exec_native_cmd,
                      get_bitbake_var, BOOTDD_EXTRA_SPACE)

logger = logging.getLogger('wic')

class BootimgEFIPlugin(SourcePlugin):
    """
    Create EFI boot partition for acrn image.
    """

    name = 'acrn-bootimg-efi'

    @classmethod
    def do_configure(cls, hdddir, creator, cr_workdir, source_params):
        """
        Create grub-efi config to manage boot option between native and acrn
        """
        bootloader = creator.ks.bootloader

        grubefi_conf = ""
        grubefi_conf += "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1\n"
        grubefi_conf += "default=acrn\n"
        grubefi_conf += "timeout=%s\n" % bootloader.timeout
        grubefi_conf += "menuentry 'acrn'{ chainloader /EFI/BOOT/acrn.efi }\n"
        grubefi_conf += "menuentry 'native'{ chainloader /EFI/BOOT/bootloaderx64.efi }\n"

        logger.debug("Writing grubefi config %s/hdd/boot/EFI/BOOT/grub.cfg",
                     cr_workdir)
        cfg = open("%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir, "w")
        cfg.write(grubefi_conf)
        cfg.close()

    @classmethod
    def do_configure_systemdboot(cls, hdddir, creator, cr_workdir, source_params):
        """
        Create loader-specific systemd-boot/gummiboot config
        """
        install_cmd = "install -d %s/loader" % hdddir
        exec_cmd(install_cmd)

        install_cmd = "install -d %s/loader/entries" % hdddir
        exec_cmd(install_cmd)

        bootloader = creator.ks.bootloader

        loader_conf = ""
        loader_conf += "default boot\n"
        loader_conf += "timeout %d\n" % bootloader.timeout

        initrd = source_params.get('initrd')

        if initrd:
            # obviously we need to have a common common deploy var
            bootimg_dir = get_bitbake_var("DEPLOY_DIR_IMAGE")
            if not bootimg_dir:
                raise WicError("Couldn't find DEPLOY_DIR_IMAGE, exiting")

            initrds = initrd.split(';')
            for rd in initrds:
                cp_cmd = "cp %s/%s %s" % (bootimg_dir, rd, hdddir)
                exec_cmd(cp_cmd, True)
        else:
            logger.debug("Ignoring missing initrd")

        logger.debug("Writing systemd-boot config "
                     "%s/hdd/boot/loader/loader.conf", cr_workdir)
        cfg = open("%s/hdd/boot/loader/loader.conf" % cr_workdir, "w")
        cfg.write(loader_conf)
        cfg.close()

        kernel = get_bitbake_var("KERNEL_IMAGETYPE")
        if get_bitbake_var("INITRAMFS_IMAGE_BUNDLE") == "1":
              if get_bitbake_var("INITRAMFS_IMAGE"):
                kernel = "%s-%s.bin" % \
                    (get_bitbake_var("KERNEL_IMAGETYPE"), get_bitbake_var("INITRAMFS_LINK_NAME"))

        title = source_params.get('title')

        boot_conf = ""
        boot_conf += "title %s\n" % (title if title else "boot")
        boot_conf += "linux /%s\n" % kernel

        label = source_params.get('label')
        label_conf = "LABEL=Boot root=%s" % creator.rootdev
        if label:
            label_conf = "LABEL=%s" % label

        boot_conf += "options %s %s\n" % \
                        (label_conf, bootloader.append)

        if initrd:
            initrds = initrd.split(';')
            for rd in initrds:
                boot_conf += "initrd /%s\n" % rd

        logger.debug("Writing systemd-boot config "
                     "%s/hdd/boot/loader/entries/boot.conf", cr_workdir)
        cfg = open("%s/hdd/boot/loader/entries/boot.conf" % cr_workdir, "w")
        cfg.write(boot_conf)
        cfg.close()


    @classmethod
    def do_configure_partition(cls, part, source_params, creator, cr_workdir,
                               oe_builddir, bootimg_dir, kernel_dir,
                               native_sysroot):
        """
        Called before do_prepare_partition(), creates loader-specific config
        """
        hdddir = "%s/hdd/boot" % cr_workdir

        install_cmd = "install -d %s/EFI/BOOT" % hdddir
        exec_cmd(install_cmd)

        cls.do_configure(hdddir, creator, cr_workdir, source_params)

        cls.do_configure_systemdboot(hdddir, creator, cr_workdir, source_params)


    @classmethod
    def do_prepare_partition(cls, part, source_params, creator, cr_workdir,
                             oe_builddir, bootimg_dir, kernel_dir,
                             rootfs_dir, native_sysroot):
        """
        Called to do the actual content population for a partition i.e. it
        'prepares' the partition to be incorporated into the image.
        In this case, prepare content for an EFI (grub) boot partition.
        """
        if not kernel_dir:
            kernel_dir = get_bitbake_var("DEPLOY_DIR_IMAGE")
            if not kernel_dir:
                raise WicError("Couldn't find DEPLOY_DIR_IMAGE, exiting")

        staging_kernel_dir = kernel_dir

        hdddir = "%s/hdd/boot" % cr_workdir

        kernel = get_bitbake_var("KERNEL_IMAGETYPE")
        if get_bitbake_var("INITRAMFS_IMAGE_BUNDLE") == "1":
            if get_bitbake_var("INITRAMFS_IMAGE"):
                kernel = "%s-%s.bin" % \
                    (get_bitbake_var("KERNEL_IMAGETYPE"), get_bitbake_var("INITRAMFS_LINK_NAME"))

        install_cmd = "install -m 0644 %s/%s %s/%s" % \
            (staging_kernel_dir, kernel, hdddir, kernel)
        exec_cmd(install_cmd)

        for mod in [x for x in os.listdir(kernel_dir) if x.startswith("grub-efi-")]:
            cp_cmd = "cp %s/%s %s/EFI/BOOT/%s" % (kernel_dir, mod, hdddir, mod[9:])
            exec_cmd(cp_cmd, True)

        for mod in [x for x in os.listdir(kernel_dir) if x.startswith("systemd-")]:
            cp_cmd = "cp %s/%s %s/EFI/BOOT/bootloaderx64.efi" % (kernel_dir, mod, hdddir)
            exec_cmd(cp_cmd, True)

        cp_cmd = "cp %s/acrn.efi %s/EFI/BOOT/" % (kernel_dir, hdddir)
        exec_cmd(cp_cmd, True)

        startup = os.path.join(kernel_dir, "startup.nsh")
        if os.path.exists(startup):
            cp_cmd = "cp %s %s/" % (startup, hdddir)
            exec_cmd(cp_cmd, True)

        du_cmd = "du -bks %s" % hdddir
        out = exec_cmd(du_cmd)
        blocks = int(out.split()[0])

        extra_blocks = part.get_extra_block_count(blocks)

        if extra_blocks < BOOTDD_EXTRA_SPACE:
            extra_blocks = BOOTDD_EXTRA_SPACE

        blocks += extra_blocks

        logger.debug("Added %d extra blocks to %s to get to %d total blocks",
                     extra_blocks, part.mountpoint, blocks)

        # dosfs image, created by mkdosfs
        bootimg = "%s/boot.img" % cr_workdir

        label = part.label if part.label else "ESP"

        dosfs_cmd = "mkdosfs -n %s -i %s -C %s %d" % \
                    (label, part.fsuuid, bootimg, blocks)
        exec_native_cmd(dosfs_cmd, native_sysroot)

        mcopy_cmd = "mcopy -i %s -s %s/* ::/" % (bootimg, hdddir)
        exec_native_cmd(mcopy_cmd, native_sysroot)

        chmod_cmd = "chmod 644 %s" % bootimg
        exec_cmd(chmod_cmd)

        du_cmd = "du -Lbks %s" % bootimg
        out = exec_cmd(du_cmd)
        bootimg_size = out.split()[0]

        part.size = int(bootimg_size)
        part.source_file = bootimg
