SUMMARY = "Package for ${IMAGE_NAME}"
# This license statement is a lie. Ideally set it to something more appropriate.
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "${PN}"

INHIBIT_DEFAULT_DEPS = "1"

# Variables to control where images are found: the multiconfig name, and the deploy dir.
CONTAINER_PACKAGE_DEPLOY_DIR ?= "${DEPLOY_DIR_IMAGE}"

# Where to install the image
containerdir ?= "${localstatedir}/lib/machines"

SRC_URI = "file://launch_zephyr.sh"

do_install() {
    install -d ${D}${containerdir}
    install ${CONTAINER_PACKAGE_DEPLOY_DIR}/zephyr.img ${D}${containerdir}/
    install -m 755 ${WORKDIR}/launch_zephyr.sh ${D}/var/lib/machines/
}

RDEPENDS_${PN} += "bash procps"
