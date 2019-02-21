require recipes-core/images/core-image-minimal.bb

CORE_IMAGE_EXTRA_INSTALL_append = " packagegroup-acrn"

inherit image-acrn
