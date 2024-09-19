inherit container-package

SRC_URI = "file://launch-weston.sh"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install:append() {
    install -m 755 ${UNPACKDIR}/launch-weston.sh ${D}/var/lib/machines/
}

RDEPENDS:${PN} += "bash procps"
