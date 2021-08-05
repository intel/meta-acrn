require recipes-core/images/core-image-base.bb

CORE_IMAGE_EXTRA_INSTALL:append = " \
    packagegroup-acrn \
    linux-firmware \
    kernel-modules \
"

inherit image-acrn
