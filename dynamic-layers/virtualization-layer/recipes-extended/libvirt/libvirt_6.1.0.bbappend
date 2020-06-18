
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_remove = "http://libvirt.org/sources/libvirt-${PV}.tar.xz;name=libvirt"
SRC_URI_remove = "file://0001-to-fix-build-error.patch"
SRC_URI_prepend = "git://github.com/projectacrn/acrn-libvirt.git;branch=${SRCBRANCH};destsuffix=libvirt-${PV};name=libvirt \
           git://gitlab.com/keycodemap/keycodemapdb.git;protocol=https;destsuffix=libvirt-${PV}/src/keycodemapdb;name=keycodemapdb "


SRCBRANCH = "dev-acrn-v6.1.0"
# with acrn v6.1.0
SRCREV_libvirt = "51be5dbc7dc8fe3142ec23aebf314aa23e109554"
SRCREV_keycodemapdb = "27acf0ef828bf719b2053ba398b195829413dbdd"

EXTRA_OECONF_append = " --disable-werror"
do_configure_prepend () {
    olddir=`pwd`
    cd ${S}
    autoreconf --verbose --force --install
    cd $olddir
}
