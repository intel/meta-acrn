require acrn-common.inc

ACRN_BOARD ?= "${MACHINE}"
ACRN_FIRMWARE ?= "uefi"

EXTRA_OEMAKE += "HV_OBJDIR=${B}/hypervisor EFI_OBJDIR=${B}/efi-stub"
EXTRA_OEMAKE += "BOARD=${ACRN_BOARD} FIRMWARE=${ACRN_FIRMWARE}"

inherit python3native deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "python3-kconfiglib-native"
DEPENDS += "${@'gnu-efi' if d.getVar('ACRN_FIRMWARE') == 'uefi' else ''}"

do_configure() {
	# Use oldconfig so we get to override more options (passed via EXTRA_OEMAKE).
	# Ideally, we could write a .config and then defoldconfig it but that doesn't work.
	# https://github.com/projectacrn/acrn-hypervisor/issues/2371
	#
	# Need to tell this explicitly where the object directory is otherwise it
	# writes the .config to S/build/hypervisor.
	oe_runmake -C hypervisor oldconfig

	# Remove the Clear Linux-ism.  When we move to 0.6 this can be done by
	# seeding .config.
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		sed -e 's|^CONFIG_UEFI_OS_LOADER_NAME=.*|CONFIG_UEFI_OS_LOADER_NAME="\\\\EFI\\\\BOOT\\\\bootx64.efi"|' -i ${B}/hypervisor/.config
	fi
}

do_compile() {
	oe_runmake -C hypervisor
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C efi-stub
	fi
}

do_install() {
	if [ "${ACRN_FIRMWARE}" = "uefi" ]; then
		oe_runmake -C efi-stub install install-debug
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
		install -m 0755 ${D}${libdir}/acrn/acrn.efi ${DEPLOYDIR}
	fi
}
