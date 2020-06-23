require acrn-devicemodel.bb
require acrn-common-dev.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/acrn-devicemodel:"
SRC_URI += "file://dont-build-tools.patch"

PROVIDES = "acrn-devicemodel"
RPROVIDES_${PN} += "acrn-devicemodel"
