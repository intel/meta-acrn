DESCRIPTION = "The xmlschema library is an implementation of XML Schema for Python (supports Python 3.6+)."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=47489cb18c469474afeb259ed1d4832f"

inherit pypi

DEPENDS += "${PYTHON_PN}-elementpath"

BBCLASSEXTEND = "native nativesdk"
