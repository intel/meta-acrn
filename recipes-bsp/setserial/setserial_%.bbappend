FILESEXTRAPATHS:prepend := "${THISDIR}/setserial:"
SRC_URI:append = " \
           file://setserial \
           file://setserial.service \
          "

inherit systemd

SYSTEMD_SERVICE:${PN} = "setserial.service"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -d ${D}${systemd_system_unitdir}
    install -m 0755 ${WORKDIR}/setserial ${D}${sysconfdir}/
    install -m 0644 ${WORKDIR}/setserial.service \
                     ${D}${systemd_system_unitdir}/setserial.service
    sed -i \
        -e 's,@SYSCONFDIR@,${sysconfdir},g' \
        -e 's,@BINDIR@,${bindir},g' \
        -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
        ${D}${sysconfdir}/setserial \
        ${D}${systemd_system_unitdir}/setserial.service
}
