IMAGE_FSTYPES += "ext4 wic.acrn"

# set the default wks for SOS image
# this can be override in local.conf by WKS_FILE_pn-<image-name>
WKS_FILE = "acrn-bootdisk-microcode.wks.in"

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
