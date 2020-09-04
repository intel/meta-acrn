require acrn-common.inc

ACRN_BOARD ?= "nuc7i7dnb"
ACRN_FIRMWARE ?= "uefi"
ACRN_SCENARIO  ?= "industry"

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor EFI_OBJDIR=${B}/efi-stub"
EXTRA_OEMAKE += "BOARD=${ACRN_BOARD} FIRMWARE=${ACRN_FIRMWARE} SCENARIO=${ACRN_SCENARIO}"
EXTRA_OEMAKE += "BOARD_FILE=${S}/misc/acrn-config/xmls/board-xmls/${ACRN_BOARD}.xml SCENARIO_FILE=${S}/misc/acrn-config/xmls/config-xmls/${ACRN_BOARD}/${ACRN_SCENARIO}.xml"

SRC_URI_append_class-target += "file://hypervisor-dont-build-pre_build.patch"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "python3-kconfiglib-native acrn-hypervisor-native acpica-native"
DEPENDS += "${@'gnu-efi' if d.getVar('ACRN_FIRMWARE') == 'uefi' else ''}"

# parallel build could face build failure in case of config-tool method:
#    | .config does not exist and no defconfig available for BOARD...
PARALLEL_MAKE = ""

do_compile_class-target() {
	# Execute natively build sanity check for ACRN configurations
	hv_prebuild_check.out
	oe_runmake -C hypervisor
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C misc/efi-stub
	fi
}

do_install_class-target() {
	oe_runmake -C hypervisor install install-debug
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C misc/efi-stub install install-debug
	fi

	# Remove sample files
	rm -rf ${D}${datadir}/acrn
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		rmdir --ignore-fail-on-non-empty ${D}${datadir}
	fi
}

FILES_${PN} += "${libdir}/acrn/"
FILES_${PN}-dbg += "${libdir}/acrn/*.efi.*"

addtask deploy after do_install before do_build
do_deploy() {
	install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_FIRMWARE}.${ACRN_SCENARIO}.32.out ${DEPLOYDIR}
	rm -f ${DEPLOYDIR}/acrn.32.out
	lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_FIRMWARE}.${ACRN_SCENARIO}.32.out ${DEPLOYDIR}/acrn.32.out

	install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_FIRMWARE}.${ACRN_SCENARIO}.bin ${DEPLOYDIR}
	rm -f ${DEPLOYDIR}/acrn.bin
	lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_FIRMWARE}.${ACRN_SCENARIO}.bin ${DEPLOYDIR}/acrn.bin

	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi ${DEPLOYDIR}
		rm -f ${DEPLOYDIR}/acrn.efi
		lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.efi ${DEPLOYDIR}/acrn.efi
	fi
	rm -f ${DEPLOYDIR}/ACPI_*.bin
	if [ -x ${D}${libdir}/acrn/acpi ]; then
		install -m 0755 ${D}${libdir}/acrn/acpi/ACPI_*.bin ${DEPLOYDIR}
	fi
}

INSANE_SKIP_${PN} += "arch already-stripped"

do_compile_class-native() {
	oe_runmake -C hypervisor pre_build
}

do_install_class-native(){
	install -d ${D}/${bindir}
	install -m 755 ${B}/hypervisor/hv_prebuild_check.out ${D}/${bindir}/hv_prebuild_check.out
}

# no action required for native to deploy
do_deploy_class-native(){
	:
}

BBCLASSEXTEND = "native "
