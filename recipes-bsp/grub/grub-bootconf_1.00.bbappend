require ${@bb.utils.contains("DISTRO_FEATURES", "ServiceVM", "acrn-grub-bootconf.inc", "", d)}
