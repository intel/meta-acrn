IMAGE_FSTYPES += "ext4 wic"

PRELAUNCHED_VM ??= ""
PRELAUNCHED_VM_MOD_TAG ??= "Zephyr_RawImage"
SOS_MOD_TAG ??= "Linux_bzImage"

WICVARS_append = " SOS_MOD_TAG PRELAUNCHED_VM PRELAUNCHED_VM_MOD_TAG  "

addtask do_deploy_prelaunched_vm before do_image after do_rootfs

do_deploy_prelaunched_vm() {
    if [ ! -z "${PRELAUNCHED_VM}" ]; then
        if [ -d ${DEPLOY_DIR_IMAGE}/${PRELAUNCHED_VM}.bin ]; then
            rm -r ${DEPLOY_DIR_IMAGE}/${PRELAUNCHED_VM}.bin
        fi
        if [ -d ${DEPLOY_DIR_IMAGE}/${PRELAUNCHED_VM}.bin ]; then
            rm -r ${IMAGE_ROOTFS}/boot/${PRELAUNCHED_VM}.bin
        fi
        install ${CONTAINER_PACKAGE_ZEPHYR_DEPLOY_DIR}/${PRELAUNCHED_VM}.bin  ${IMAGE_ROOTFS}/boot/
        install ${CONTAINER_PACKAGE_ZEPHYR_DEPLOY_DIR}/${PRELAUNCHED_VM}.bin  ${DEPLOY_DIR_IMAGE}/
    fi
}
