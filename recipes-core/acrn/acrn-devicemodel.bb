require acrn-common.inc

SRC_URI += "file://dont-build-tools.patch"

inherit python3native

DEPENDS += "python3-kconfiglib-native util-linux libusb1 openssl libpciaccess acrn-tools"

# Tell the build where to find acrn-tools
EXTRA_OEMAKE += "TOOLS_OUT=${STAGING_DIR_TARGET}${includedir}/acrn"

EXTRA_OEMAKE += "ASL_COMPILER=${bindir}/iasl"

RDEPENDS_${PN} += "acpica"

FILES_${PN} += "${datadir}/acrn"

do_compile() {
	oe_runmake devicemodel
}

do_install() {
	oe_runmake devicemodel-install

	# Write a modprobe.d so that acrngt is loaded before i915, as otherwise i915
	# fails to initialise and output is disabled.
	install -d ${D}${sysconfdir}/modprobe.d
	echo "softdep i915 pre: acrngt" >${D}${sysconfdir}/modprobe.d/acrn.conf

	# Remove samples, these should be packaged separately.
	rm -rf ${D}${systemd_unitdir}
	rmdir --ignore-fail-on-non-empty `dirname ${D}${systemd_unitdir}`
	rm -rf ${D}${datadir}/acrn/samples
	rmdir --ignore-fail-on-non-empty ${D}${datadir}
}
