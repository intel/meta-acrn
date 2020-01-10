require acrn-common.inc

ACRN_BOARD ?= "nuc7i7dnb"
ACRN_FIRMWARE ?= "uefi"
ACRN_SCENARIO  ?= "sdc"

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor EFI_OBJDIR=${B}/efi-stub"
EXTRA_OEMAKE += "BOARD=${ACRN_BOARD} FIRMWARE=${ACRN_FIRMWARE} SCENARIO=${ACRN_SCENARIO}"
EXTRA_OEMAKE += "BOARD_FILE=${S}/misc/acrn-config/xmls/board-xmls/${ACRN_BOARD}.xml SCENARIO_FILE=${S}/misc/acrn-config/xmls/config-xmls/${ACRN_BOARD}/${ACRN_SCENARIO}.xml"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"


DEPENDS += "python3-kconfiglib-native"
DEPENDS += "${@'gnu-efi' if d.getVar('ACRN_FIRMWARE') == 'uefi' else ''}"

do_configure() {
	mkdir --parents ${B}/hypervisor
	cat <<-EOF >> ${B}/hypervisor/.config
CONFIG_BOARD="${ACRN_BOARD}"
CONFIG_$(echo ${ACRN_SCENARIO} | tr '[:lower:]' '[:upper:]')=y
CONFIG_UEFI_OS_LOADER_NAME="\\\\EFI\\\\BOOT\\\\bootx64.efi"
EOF
	cat ${B}/hypervisor/.config
	oe_runmake -C hypervisor oldconfig
}


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
