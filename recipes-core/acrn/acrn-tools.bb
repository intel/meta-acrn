require acrn-common.inc

# TODO: force this to 1 for now as debug mode means a dependency on telemetrics
ACRN_RELEASE = "1"

inherit pkgconfig systemd

PACKAGECONFIG ??= "${@'debugtools' if d.getVar('ACRN_RELEASE') == '0' else ''}"
PACKAGECONFIG[debugtools] = ",,telemetrics e2fsprogs libxml2 systemd util-linux openssl,bash"

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

SYSTEMD_SERVICE_${PN} = "acrnd.service acrnlog.service"
SYSTEMD_SERVICE_${PN} += "${@'usercrash.service acrnprobe.service' if d.getVar('ACRN_RELEASE') == '0' else ''}"

FILES_${PN} += "${systemd_unitdir} ${libdir}/tmpfiles.d ${datadir}/acrn ${datadir}/defaults"
