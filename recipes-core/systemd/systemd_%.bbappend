# TODO: shouldn't do this unless enabled
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://acrn.patch;maxver=241"
