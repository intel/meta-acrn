#
# Copyright (c) 2020, Intel Corporation.
#
# SPDX-License-Identifier: GPL-2.0-only
#
# DESCRIPTION
# This implements the 'acrn-bootimg-efi' source plugin class for 'wic'.
# This source plugin should default with grub-efi
# AUTHORS
# Lee Chee Yang <chee.yang.lee (at] intel.com>
#

import logging
import os
import shutil
import re

from glob import glob

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
    def do_configure_grubefi(cls, hdddir, creator, cr_workdir, source_params):
        """
        Create grub.cfg
        """
        configfile = creator.ks.bootloader.configfile
        custom_cfg = None
        if configfile:
            custom_cfg = get_custom_config(configfile)
            if custom_cfg:
                # Use a custom configuration
                grubefi_conf = custom_cfg
                logger.debug("Using custom configuration file "
                             "%s for grub.cfg", configfile)
            else:
                raise WicError("configfile is specified but failed to "
                               "get it from %s." % configfile)

        initrd = source_params.get('initrd')

        if initrd:
            bootimg_dir = get_bitbake_var("DEPLOY_DIR_IMAGE")
            if not bootimg_dir:
                raise WicError("Couldn't find DEPLOY_DIR_IMAGE, exiting")

            initrds = initrd.split(';')
            for rd in initrds:
                cp_cmd = "cp %s/%s %s" % (bootimg_dir, rd, hdddir)
                exec_cmd(cp_cmd, True)
        else:
            logger.debug("Ignoring missing initrd")

        if not custom_cfg:
            # Create grub configuration using parameters from wks file
            bootloader = creator.ks.bootloader
            title = source_params.get('title')

            grubefi_conf = ""
            grubefi_conf += "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1\n"
            grubefi_conf += "default='ACRN (Yocto)'\n"
            grubefi_conf += "timeout=%s\n" % bootloader.timeout
            grubefi_conf += "menuentry '%s'{\n" % (title if title else "boot")

            kernel = get_bitbake_var("KERNEL_IMAGETYPE")
            if get_bitbake_var("INITRAMFS_IMAGE_BUNDLE") == "1":
                if get_bitbake_var("INITRAMFS_IMAGE"):
                    kernel = "%s-%s.bin" % \
                        (get_bitbake_var("KERNEL_IMAGETYPE"), get_bitbake_var("INITRAMFS_LINK_NAME"))

            label = source_params.get('label')
            label_conf = "root=%s" % creator.rootdev
            if label:
                label_conf = "LABEL=%s" % label

            grubefi_conf += "linux /%s %s rootwait %s\n" \
                % (kernel, label_conf, bootloader.append)

            if initrd:
                initrds = initrd.split(';')
                grubefi_conf += "initrd"
                for rd in initrds:
                    grubefi_conf += " /%s" % rd
                grubefi_conf += "\n"

            grubefi_conf += "}\n"
            grubefi_conf += "menuentry 'ACRN (Yocto)'{\n"

            aux_modules = get_bitbake_var("ACRN_EFI_GRUB2_MOD_CFG")
            if aux_modules:
                aux_modules = aux_modules.split(";")
                for aux_module in aux_modules:
                    if not aux_module:
                        continue
                    grubefi_conf += "%s\n" % aux_module

            acrn_bootparams = get_bitbake_var("ACRN_HV_EFI_CFG")
            if acrn_bootparams:
                grubefi_conf += "\nmultiboot2 /acrn.bin %s %s \n" % \
                    (label_conf, acrn_bootparams)
            else:
                grubefi_conf += "\nmultiboot2 /acrn.bin %s \n" % \
                    (label_conf)

            boot_confs = get_bitbake_var("ACRN_EFI_BOOT_CONF").split(";")
            for boot_conf in boot_confs:
                if not boot_conf:
                     continue
                conf = boot_conf.split(":")
                if not len(conf) in [2,3,4]:
                    raise WicError("unable to parse ACRN_EFI_BOOT_CONF, in \"%s\" (unexpected parameter count: %i) exiting" \
                        % boot_conf, len(conf) )

                search_param = "--file %s" %(conf[0])
                kernel_param = "%s" %(conf[0])
                module_param = "%s" %(conf[1])

                if len(conf) > 2 and conf[2] != "":
                    module_param = "%s %s\n" %(conf[1] ,conf[2])
                if len(conf) > 3 and conf[3] != "":
                    search_param = "--label %s" %(conf[3])

                grubefi_conf += "search --set=mpath %s \n" %(search_param)
                grubefi_conf += "set modbin=($mpath)%s \n" %(kernel_param)
                grubefi_conf += "module2 $modbin %s\n" %(module_param)

            grubefi_conf += "}\n"

        logger.debug("Writing grubefi config %s/hdd/boot/EFI/BOOT/grub.cfg",
                     cr_workdir)
        with open("%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir, "w") as cfg:
            cfg.write(grubefi_conf)

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

        cls.do_configure_grubefi(hdddir, creator, cr_workdir, source_params)

        if get_bitbake_var("IMAGE_EFI_BOOT_FILES") is None:
            logger.debug('No boot files defined in IMAGE_EFI_BOOT_FILES')
        else:
            boot_files = None
            for (fmt, id) in (("_uuid-%s", part.uuid), ("_label-%s", part.label), (None, None)):
                if fmt:
                    var = fmt % id
                else:
                    var = ""

                boot_files = get_bitbake_var("IMAGE_EFI_BOOT_FILES" + var)
                if boot_files:
                    break

            logger.debug('Boot files: %s', boot_files)

            # list of tuples (src_name, dst_name)
            deploy_files = []
            for src_entry in re.findall(r'[\w;\-\./\*]+', boot_files):
                if ';' in src_entry:
                    dst_entry = tuple(src_entry.split(';'))
                    if not dst_entry[0] or not dst_entry[1]:
                        raise WicError('Malformed boot file entry: %s' % src_entry)
                else:
                    dst_entry = (src_entry, src_entry)

                logger.debug('Destination entry: %r', dst_entry)
                deploy_files.append(dst_entry)

            cls.install_task = [];
            for deploy_entry in deploy_files:
                src, dst = deploy_entry
                if '*' in src:
                    # by default install files under their basename
                    entry_name_fn = os.path.basename
                    if dst != src:
                        # unless a target name was given, then treat name
                        # as a directory and append a basename
                        entry_name_fn = lambda name: \
                                        os.path.join(dst,
                                                     os.path.basename(name))

                    srcs = glob(os.path.join(kernel_dir, src))

                    logger.debug('Globbed sources: %s', ', '.join(srcs))
                    for entry in srcs:
                        src = os.path.relpath(entry, kernel_dir)
                        entry_dst_name = entry_name_fn(entry)
                        cls.install_task.append((src, entry_dst_name))
                else:
                    cls.install_task.append((src, dst))

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

        install_cmd = "install -m 0644 %s/acrn.bin %s/acrn.bin" % \
            (staging_kernel_dir, hdddir)
        exec_cmd(install_cmd)

        if get_bitbake_var("IMAGE_EFI_BOOT_FILES"):
            for src_path, dst_path in cls.install_task:
                install_cmd = "install -m 0644 -D %s %s" \
                              % (os.path.join(kernel_dir, src_path),
                                 os.path.join(hdddir, dst_path))
                exec_cmd(install_cmd)

        for mod in [x for x in os.listdir(kernel_dir) if x.startswith("grub-efi-")]:
            cp_cmd = "cp %s/%s %s/EFI/BOOT/%s" % (kernel_dir, mod, hdddir, mod[9:])
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
