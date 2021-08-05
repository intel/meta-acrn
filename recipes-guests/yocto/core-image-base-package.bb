inherit container-package

SRC_URI = "file://launch-base.sh"

do_install:append() {
    install -m 755 ${WORKDIR}/launch-base.sh ${D}/var/lib/machines/
}

RDEPENDS:${PN} += "bash procps"
