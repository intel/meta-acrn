SUMMARY = "A Type 1 hypervisor stack, running directly on the bare-metal hardware"
HOMEPAGE = "https://projectacrn.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5732af37bf18525ed9d2b16985054901"

SRC_URI = "git://github.com/projectacrn/acrn-hypervisor.git;branch=release_0.6 \
           file://paths.patch"

PV = "0.6"
SRCREV = "35f20a5a291f1836e5bd6dfb7ee7398f0b078b4c"

UPSTREAM_CHECK_COMMITS = "1"

S = "${WORKDIR}/git/"

# 1 for release build, 0 for debug build.
ACRN_RELEASE ?= "1"

EXTRA_OEMAKE += "RELEASE=${ACRN_RELEASE} \
				 SYSROOT=${STAGING_DIR_TARGET} \
				 O=${B} \
				 DESTDIR=${D} \
				 prefix=${prefix} \
				 bindir=${bindir} \
				 libdir=${libdir} \
				 datadir=${datadir} \
				 includedir=${includedir} \
				 systemd_unitdir=${systemd_unitdir}"


# acrn supports build objects out-of-tree but builds must be performed from
# inside the source
B = "${WORKDIR}/build/"
do_configure[cleandirs] = "${B}"
do_configure[dirs] = "${S}"
do_compile[dirs] = "${S}"
do_install[dirs] = "${S}"
