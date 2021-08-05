SUMMARY = "Configuration file for networkd"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://80-dhcp.network"

do_install() {
    install -d ${D}${nonarch_base_libdir}/systemd/network
    install -m644 ${WORKDIR}/*.network ${D}${nonarch_base_libdir}/systemd/network
}

FILES:${PN} = "${nonarch_base_libdir}/systemd/network"
