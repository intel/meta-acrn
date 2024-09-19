inherit container-package

SRC_URI = "file://launch-base.sh"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install:append() {
    install -m 755 ${UNPACKDIR}/launch-base.sh ${D}/var/lib/machines/
}

RDEPENDS:${PN} += "bash procps"
