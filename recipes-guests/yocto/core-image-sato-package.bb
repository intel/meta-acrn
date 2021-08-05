inherit container-package

SRC_URI = "file://launch-sato.sh"

do_install:append() {
    install -m 755 ${WORKDIR}/launch-sato.sh ${D}/var/lib/machines/
}

RDEPENDS:${PN} += "bash procps"
