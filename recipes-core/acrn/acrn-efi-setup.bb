SUMMARY = "EFI configuration for ACRN"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://acrn-efi-setup.sh;beginline=2;endline=3;md5=25bf7f1592f9018e1d79621845443e20"

SRC_URI = "file://acrn-efi-setup.sh"
S = "${WORKDIR}"

do_install() {
	install -d ${D}${sbindir}
	install -m0755 ${WORKDIR}/acrn-efi-setup.sh ${D}${sbindir}
}

RDEPENDS_${PN} = "efibootmgr perl"

pkg_postinst_ontarget_${PN} () {
	acrn-efi-setup.sh
}
