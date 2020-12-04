require acrn-common.inc

ACRN_BOARD ?= "nuc7i7dnb"
ACRN_SCENARIO  ?= "industry"
ACRN_BOARD_FILE ?= ""
ACRN_SCENARIO_FILE ?= ""

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor "
EXTRA_OEMAKE += "${@bb.utils.contains('ACRN_BOARD_FILE', '', 'BOARD_FILE=${ACRN_BOARD_FILE}', 'BOARD=${ACRN_BOARD}', d)}"
EXTRA_OEMAKE += "${@bb.utils.contains('ACRN_SCENARIO_FILE', '', 'SCENARIO_FILE=${ACRN_SCENARIO_FILE}', 'SCENARIO=${ACRN_SCENARIO}', d)} "

SRC_URI_append_class-target += "file://hypervisor-dont-build-pre_build.patch"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "python3-kconfiglib-native acrn-hypervisor-native acpica-native"

# parallel build could face build failure in case of config-tool method:
#    | .config does not exist and no defconfig available for BOARD...
PARALLEL_MAKE = ""

do_compile_class-target() {
	# Execute natively build sanity check for ACRN configurations
	hv_prebuild_check.out
	oe_runmake -C hypervisor
}

do_install_class-target() {
	oe_runmake -C hypervisor install install-debug

	# Remove sample files
	rm -rf ${D}${datadir}/acrn
}

FILES_${PN} += "${libdir}/acrn/"

addtask deploy after do_install before do_build
do_deploy() {
	install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.32.out ${DEPLOYDIR}
	rm -f ${DEPLOYDIR}/acrn.32.out
	lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.32.out ${DEPLOYDIR}/acrn.32.out

	install -m 0755 ${D}${libdir}/acrn/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.bin ${DEPLOYDIR}
	rm -f ${DEPLOYDIR}/acrn.bin
	lnr ${DEPLOYDIR}/acrn.${ACRN_BOARD}.${ACRN_SCENARIO}.bin ${DEPLOYDIR}/acrn.bin

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
