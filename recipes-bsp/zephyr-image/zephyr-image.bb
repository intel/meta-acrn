SUMMARY = "Create zephyr.img having grub, application image and grub config"
LICENSE = "CLOSED"
inherit deploy
do_deploy[depends] += "dosfstools-native:do_populate_sysroot \
                        mtools-native:do_populate_sysroot \
                        "

SRC_URI = "file://grub.cfg"
DEPENDS += " zephyr-grub-efi"

#Below variables must be set in local.conf
CONTAINER_PACKAGE_DEPLOY_DIR ??=""
CONTAINER_PACKAGE_ZEPHYR_DEPLOY_DIR ??=""
CONTAINER_PACKAGE_MC ?= ""
ZEPHYR_APP ??=""

do_deploy[mcdepends] += "multiconfig::${CONTAINER_PACKAGE_MC}:zephyr-grub-efi:do_deploy"

do_deploy () {
    if [ -d ${DEPLOYDIR}/efi ]; then
        rm -r ${DEPLOYDIR}/efi
    fi

    if [ -d ${DEPLOYDIR}/kernel ]; then
        rm -r ${DEPLOYDIR}/kernel
    fi
    if [ -e ${DEPLOYDIR}/zephyr.img ]; then
        rm ${DEPLOYDIR}/zephyr.img
    fi

    mkdir ${DEPLOYDIR}/efi
    mkdir ${DEPLOYDIR}/kernel
    mkdir ${DEPLOYDIR}/efi/boot
    cp ${CONTAINER_PACKAGE_DEPLOY_DIR}/grub-efi-bootx64.efi ${DEPLOYDIR}/efi/boot/bootx64.efi
    cp ${CONTAINER_PACKAGE_ZEPHYR_DEPLOY_DIR}/${ZEPHYR_APP}.elf ${DEPLOYDIR}/kernel/zephyr.elf
    cp ${WORKDIR}/grub.cfg ${DEPLOYDIR}/efi/boot/
    mkdosfs -F 32 -C ${DEPLOYDIR}/zephyr.img 35840
    mcopy -i ${DEPLOYDIR}/zephyr.img -s ${DEPLOYDIR}/efi ${DEPLOYDIR}/kernel ::/
    chmod 644 ${DEPLOYDIR}/zephyr.img
    rm -rf ${DEPLOYDIR}/kernel ${DEPLOYDIR}/efi
}

addtask deploy after do_compile
do_install[noexec] = "1"
