SUMMARY = "Package for ${IMAGE_NAME}"
# This license statement is a lie. Ideally set it to something more appropriate.
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "${PN}"

INHIBIT_DEFAULT_DEPS = "1"

# Variables to control where images are found: the multiconfig name, and the deploy dir.
CONTAINER_PACKAGE_MC ?= ""
CONTAINER_PACKAGE_DEPLOY_DIR ?= "${DEPLOY_DIR_IMAGE}"

# The name of the image
IMAGE_NAME := "${@d.getVar('PN').replace('-package', '')}"
# Where to install the image
containerdir ?= "${localstatedir}/lib/machines"

do_install[mcdepends] += "multiconfig:${BB_CURRENT_MC}:${CONTAINER_PACKAGE_MC}:${IMAGE_NAME}:do_image_complete"

do_install () {
	install -d ${D}${containerdir}
	install ${CONTAINER_PACKAGE_DEPLOY_DIR}/${IMAGE_NAME}-${MACHINE}.wic ${D}${containerdir}/${IMAGE_NAME}.wic
}
