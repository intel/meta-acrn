require recipes-core/images/core-image-base.bb

CORE_IMAGE_EXTRA_INSTALL_append = " \
    linux-firmware \
    kernel-modules \
    acrn-hypervisor \
    acrn-tools \
    acrn-devicemodel \
    acrn-efi-setup \
"

inherit image-acrn
IMAGE_FSTYPES = "ext4 wic.acrn"
