SUMMARY = "ACRN hypervisor"

inherit packagegroup distro_features_check

# Currently requires systemd as the networking glue is systemd-specific
REQUIRED_DISTRO_FEATURES = "systemd"

RDEPENDS_${PN} = "\
    acrn-hypervisor \
    acrn-tools \
    acrn-devicemodel \
    acrn-efi-setup \
    "
