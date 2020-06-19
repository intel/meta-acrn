require acrn-common.inc

inherit features_check systemd

REQUIRED_DISTRO_FEATURES = "systemd"

EXTRA_OEMAKE += " OUT_DIR=${B} "

do_compile() {
	oe_runmake -C misc/life_mngr
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${B}/life_mngr ${D}${bindir}
	install -d ${D}${systemd_unitdir}/system/
	install -m 0644 ${B}/life_mngr.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} = "life_mngr.service"
