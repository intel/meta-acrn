inherit container-package

SRC_URI = "file://launch-sato.sh"

do_install_append() {
    install -m 755 ${WORKDIR}/launch-sato.sh ${D}/var/lib/machines/
}

RDEPENDS_${PN} += "bash procps"
