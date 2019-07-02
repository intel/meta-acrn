require recipes-graphics/images/core-image-weston.bb

CORE_IMAGE_EXTRA_INSTALL_append = " \
    packagegroup-acrn \
    linux-firmware \
    kernel-modules \
"

inherit image-acrn
