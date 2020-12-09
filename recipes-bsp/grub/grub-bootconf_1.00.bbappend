require ${@bb.utils.contains("DISTRO_FEATURES", "SOS", "acrn-grub-bootconf.inc", "", d)}
