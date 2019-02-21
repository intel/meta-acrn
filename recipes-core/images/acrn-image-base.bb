require recipes-core/images/core-image-base.bb

CORE_IMAGE_EXTRA_INSTALL_append = " \
    packagegroup-acrn \
    linux-firmware \
    kernel-modules \
"

inherit image-acrn
IMAGE_FSTYPES = "ext4 wic.acrn"
