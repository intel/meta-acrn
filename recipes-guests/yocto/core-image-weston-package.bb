inherit container-package

SRC_URI = "file://launch-weston.sh"

do_install_append() {
    install -m 755 ${WORKDIR}/launch-weston.sh ${D}/var/lib/machines/
}

RDEPENDS_${PN} += "bash procps"
