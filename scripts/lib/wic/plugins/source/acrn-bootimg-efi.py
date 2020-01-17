#
# Copyright (c) 2020, Intel Corporation.
#
# SPDX-License-Identifier: GPL-2.0-only
#
# DESCRIPTION
# This implements the 'acrn-bootimg-efi' source plugin class for 'wic'
# This source plugin implement existing feature from 'bootimg-efi' with extra
# steps to populate acrn.efi
#
# AUTHORS
# Lee Chee Yang <chee.yang.lee (at] linux.intel.com>
#
import sys
import os
import shutil
import types

from wic import WicError
from wic.pluginbase import SourcePlugin
from wic.misc import (exec_cmd, exec_native_cmd,
                      get_bitbake_var, BOOTDD_EXTRA_SPACE)

from importlib.machinery import SourceFileLoader

class AcrnBootimgEFIPlugin(SourcePlugin):
    """
    Create EFI boot partition for acrn image.
    """

    name = 'acrn-bootimg-efi'
    _SOURCE_PLUGIN_DIR = "scripts/lib/wic/plugins/source/"
    _DEFAULT_EFI_MODULE_NAME = "bootimg-efi"
    _BootimgEFIObj = None

    @classmethod
    def __instanciateSubClasses(cls):
        """
        Import bootimg-efi (class name "BootimgEFIPlugin")
        """
        modulePath = None
        # look for source plugin bootimg-efi
        for libPath in sys.path:
            if os.path.exists(os.path.join(libPath, cls._SOURCE_PLUGIN_DIR +
                                cls._DEFAULT_EFI_MODULE_NAME + ".py")):
                modulePath = os.path.join(libPath, cls._SOURCE_PLUGIN_DIR +
                                    cls._DEFAULT_EFI_MODULE_NAME + ".py")
                break

        if not modulePath:
            raise WicError("Couldn't find source plugin bootimg-efi, exiting")

        loader = SourceFileLoader(cls._DEFAULT_EFI_MODULE_NAME, modulePath)
        mod = types.ModuleType(loader.name)
        loader.exec_module(mod)
        cls._BootimgEFIObj = mod.BootimgEFIPlugin()

    @classmethod
    def do_configure_partition(cls, part, source_params, creator, cr_workdir,
                               oe_builddir, bootimg_dir, kernel_dir,
                               native_sysroot):
        """
        Called before do_prepare_partition()
        """
        # include INITRD and INITRD_LIVE as initrd
        if get_bitbake_var("INITRD_LIVE"):
            if 'initrd' in source_params:
                source_params['initrd'] = "%s;%s" % \
                            (os.path.basename(get_bitbake_var("INITRD_LIVE")),
                            source_params['initrd'] )
            else:
                source_params['initrd'] = os.path.basename(get_bitbake_var("INITRD_LIVE"))

        if get_bitbake_var("INITRD"):
            if 'initrd' in source_params:
                source_params['initrd'] = "%s;%s" % \
                            (os.path.basename(get_bitbake_var("INITRD")),
                            source_params['initrd'] )
            else:
                source_params['initrd'] = os.path.basename(get_bitbake_var("INITRD"))

        if (not cls._BootimgEFIObj):
            cls.__instanciateSubClasses()

        hdddir = "%s/hdd/boot" % cr_workdir

        install_cmd = "install -d %s/EFI/BOOT" % hdddir
        exec_cmd(install_cmd)

        try:
            if source_params['loader'] == 'grub-efi':
                cls._BootimgEFIObj.do_configure_grubefi(hdddir, creator, cr_workdir, source_params)
            elif source_params['loader'] == 'systemd-boot':
                cls._BootimgEFIObj.do_configure_systemdboot(hdddir, creator, cr_workdir, source_params)
            else:
                raise WicError("unrecognized bootimg-efi loader: %s" % source_params['loader'])
        except KeyError:
            raise WicError("bootimg-efi requires a loader, none specified")

    @classmethod
    def do_prepare_partition(cls, part, source_params, creator, cr_workdir,
                             oe_builddir, bootimg_dir, kernel_dir,
                             rootfs_dir, native_sysroot):
        """
        Called to do the actual content population for a partition
        """
        if not kernel_dir:
            kernel_dir = get_bitbake_var("DEPLOY_DIR_IMAGE")
            if not kernel_dir:
                raise WicError("Couldn't find DEPLOY_DIR_IMAGE, exiting")

        hdddir = "%s/hdd/boot" % cr_workdir

        # add extra partition space of acrn.efi size(block)
        du_cmd = "du -bks %s/acrn.efi" % kernel_dir
        out = exec_cmd(du_cmd)
        BOOTDD_EXTRA_SPACE = int(out.split()[0]) * 2

        if (not cls._BootimgEFIObj):
            cls.__instanciateSubClasses()

        cls._BootimgEFIObj.do_prepare_partition(
            part, source_params, creator, cr_workdir,
            oe_builddir, bootimg_dir, kernel_dir,
            rootfs_dir, native_sysroot)

        bootimg = "%s/boot.img" % cr_workdir

        mcopy_cmd = "mcopy -i %s -s %s/acrn.efi ::/EFI/BOOT/acrn.efi" \
                     % (bootimg, kernel_dir)
        exec_native_cmd(mcopy_cmd, native_sysroot)
