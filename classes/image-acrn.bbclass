IMAGE_FSTYPES += "ext4 wic.acrn"

# Conversion command to inject acrn.efi.  TODO should be done by wic directly.

CONVERSIONTYPES_append = " acrn"

CONVERSION_DEPENDS_acrn = "acrn-hypervisor:do_deploy"

CONVERSION_CMD_acrn () {
    # Add acrn.efi as the default efi image. TODO generalise!
    if [ -f ${DEPLOY_DIR_IMAGE}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi ]; then
        cp ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.acrn

        wic cp \
            --native-sysroot ${STAGING_DIR_NATIVE} \
            ${DEPLOY_DIR_IMAGE}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi \
            ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.acrn:1/EFI/BOOT/acrn.efi
    else
        bberror "Asked to generate acrn image, but acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi not found"
    fi
}

inherit acrn
