require acrn-common.inc

ACRN_BOARD ?= "nuc11tnbi5"
ACRN_SCENARIO  ?= "shared"
ACRN_CONFIG_PATCH ?= ""

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor "
EXTRA_OEMAKE += "BOARD=${ACRN_BOARD} SCENARIO=${ACRN_SCENARIO}"
EXTRA_OEMAKE += "EFI_OBJDIR=${B}/misc/efi-stub"

SRC_URI:append:class-target = " file://hypervisor-dont-build-pre_build.patch"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "acrn-hypervisor-native acpica-native python3-lxml-native gnu-efi python3-defusedxml-native"

# parallel build could face build failure in case of config-tool method:
#    | .config does not exist and no defconfig available for BOARD...
PARALLEL_MAKE = ""

do_configure:class-target() {
	# generate configuration and patch it when the configuration patch file is valid
	# clang-format are not supported at this point, hence the patch file to use should not generate with clang-format
	if [ -n "${ACRN_CONFIG_PATCH}" ]; then
		if [ -f "${ACRN_CONFIG_PATCH}" ]; then
			oe_runmake hvdefconfig
			oe_runmake hvapplydiffconfig PATCH=${ACRN_CONFIG_PATCH}
		else
			bberror "ACRN_CONFIG_PATCH are set to ${ACRN_CONFIG_PATCH} but the file not found"
		fi
	fi
}

do_compile:class-target() {
	# Execute natively build sanity check for ACRN configurations
	hv_prebuild_check.out
	oe_runmake -C hypervisor

	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C misc/efi-stub
	fi
}

do_install:class-target() {
	oe_runmake -C hypervisor install install-debug

	if [ -x ${B}/misc/efi-stub/boot.efi ]; then
		install -m 0755 ${B}/misc/efi-stub/boot.efi ${D}${libdir}/acrn/
	fi

	# Remove sample files
	rm -rf ${D}${datadir}/acrn
}

FILES:${PN} += "${libdir}/acrn/"

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

	if [ -x ${D}${libdir}/acrn/boot.efi ]; then
		install -m 0755 ${D}${libdir}/acrn/boot.efi ${DEPLOYDIR}
	fi
}

INSANE_SKIP:${PN} += "arch already-stripped"

do_compile:class-native() {
	oe_runmake -C hypervisor pre_build
}

do_install:class-native(){
	install -d ${D}/${bindir}
	install -m 755 ${B}/hypervisor/hv_prebuild_check.out ${D}/${bindir}/hv_prebuild_check.out
}

# no action required for native to deploy
do_deploy:class-native(){
	:
}

BBCLASSEXTEND = "native "
