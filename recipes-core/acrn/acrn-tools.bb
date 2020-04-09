require acrn-common.inc

inherit pkgconfig systemd

SRC_URI += "file://avoid-race-condition.patch"

DEPENDS += "numactl systemd e2fsprogs libevent"
RDEPENDS_${PN} += "bash"

do_compile() {
	oe_runmake tools
}

do_install() {
	oe_runmake tools-install

	# tmpfiles.d is useless without systemd
	if ! ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		rm -rf ${D}${libdir}/tmpfiles.d
	fi
}

SYSTEMD_SERVICE_${PN} = "acrnd.service"
SYSTEMD_SERVICE_${PN} += "${@'acrnlog.service' if d.getVar('ACRN_RELEASE') == '0' else ''}"
SYSTEMD_SERVICE_${PN} += "${@'acrnprobe.service' if d.getVar('ACRN_RELEASE') == '0' else ''}"
SYSTEMD_SERVICE_${PN} += "${@'usercrash.service' if d.getVar('ACRN_RELEASE') == '0' else ''}"

FILES_${PN} += "${systemd_unitdir} ${libdir}/tmpfiles.d ${datadir}/acrn ${datadir}/defaults"
