require acrn-common.inc

inherit pkgconfig systemd

DEPENDS += "numactl systemd e2fsprogs libevent libxml2 openssl"
RDEPENDS:${PN} += "bash"

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

SYSTEMD_SERVICE:${PN} = "acrnd.service"
SYSTEMD_SERVICE:${PN} += "${@'acrnlog.service' if d.getVar('ACRN_RELEASE') == 'n' else ''}"
SYSTEMD_SERVICE:${PN} += "${@'acrnprobe.service' if d.getVar('ACRN_RELEASE') == 'n' else ''}"
SYSTEMD_SERVICE:${PN} += "${@'usercrash.service' if d.getVar('ACRN_RELEASE') == 'n' else ''}"

FILES:${PN} += "${systemd_unitdir} ${libdir}/tmpfiles.d ${datadir}/acrn ${datadir}/defaults"
