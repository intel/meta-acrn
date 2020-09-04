IMAGE_FSTYPES += "ext4 wic.acrn"

# in format of
# <kernel image>:<VMx kern_mod>:<bootarg>;
# for each module, split each module with semicolon.
# below example show zephyr.bin as VM0 without bootargs and
# bzImage as VM1 with bootargs eg :
# ACRN_EFI_BOOT_CONF ?= "zephyr.bin:Zephyr_RawImage;bzImage:Linux_bzImage:rootwait root=/dev/sda1;"
ACRN_EFI_BOOT_CONF ?= "${KERNEL_IMAGETYPE}:Linux_bzImage;"

WICVARS_append = " ACRN_EFI_BOOT_CONF IMAGE_EFI_BOOT_FILES "

# Conversion command to inject acrn.efi.  TODO should be done by wic directly.

CONVERSIONTYPES_append = " acrn"

CONVERSION_DEPENDS_acrn = "acrn-hypervisor:do_deploy"

CONVERSION_CMD_acrn () {
    # Add acrn.efi as the default efi image. TODO generalise!
    if [ -f ${DEPLOY_DIR_IMAGE}/acrn.efi ]; then
        cp ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.acrn

        wic cp \
            --native-sysroot ${STAGING_DIR_NATIVE} \
            ${DEPLOY_DIR_IMAGE}/acrn.efi \
            ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.acrn:1/EFI/BOOT/acrn.efi
    else
        bberror "Asked to generate acrn image, but no acrn.efi found"
    fi
}
