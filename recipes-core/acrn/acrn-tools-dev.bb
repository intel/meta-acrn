require acrn-tools.bb
require acrn-common-dev.inc

SRC_URI_remove = "file://avoid-race-condition.patch"

PROVIDES = "acrn-tools"
RPROVIDES_${PN} += "acrn-tools"
