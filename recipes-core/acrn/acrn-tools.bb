require acrn-common.inc

SRC_URI += "file://no-crashlog.patch"

inherit pkgconfig systemd

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
# Add usercrash.service acrnprobe.service below when enabling crashlog
SYSTEMD_SERVICE_${PN} += "${@'acrnlog.service' if d.getVar('ACRN_RELEASE') == '0' else ''}"

FILES_${PN} += "${systemd_unitdir} ${libdir}/tmpfiles.d ${datadir}/acrn ${datadir}/defaults"
