require acrn-common.inc

SRC_URI += "file://efi-loader.patch"

ACRN_BOARD ?= "whl-ipc-i7"
ACRN_FIRMWARE ?= "uefi"
ACRN_SCENARIO  ?= "industry"

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor EFI_OBJDIR=${B}/efi-stub"
EXTRA_OEMAKE += "BOARD=${ACRN_BOARD} FIRMWARE=${ACRN_FIRMWARE} SCENARIO=${ACRN_SCENARIO}"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"


DEPENDS += "python3-kconfiglib-native"
DEPENDS += "${@'gnu-efi' if d.getVar('ACRN_FIRMWARE') == 'uefi' else ''}"


do_compile() {
	oe_runmake hypervisor
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C misc/efi-stub
	fi
}

do_install() {
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C misc/efi-stub install install-debug
	else
		oe_runmake -C hypervisor install install-debug
	fi

	# Remove sample files
	rm -rf ${D}${datadir}/acrn
	rmdir --ignore-fail-on-non-empty ${D}${datadir}
}

FILES_${PN} += "${libdir}/acrn/"
FILES_${PN}-dbg += "${libdir}/acrn/*.efi.*"

addtask deploy after do_install before do_build
do_deploy() {
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi ${DEPLOYDIR}
		rm -f ${DEPLOYDIR}/acrn.efi
		lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi ${DEPLOYDIR}/acrn.efi
	fi
}
