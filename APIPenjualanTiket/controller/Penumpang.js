const penumpang = require('../model/Penumpang.js')
const response = require('../config/response')
const mongoose = require('mongoose')
const ObjectId = mongoose.Types.ObjectId

exports.inputDataPenumpang = (data, gambar) =>
    new Promise(async (resolve, reject)=>{

        const penumpangBaru = new penumpang({
            kodePenumpang      :data.kodePenumpang,
            namaPenumpang      :data.namaPenumpang,
            umur               :data.umur,
            tujuan             :data.tujuan,
            tanggalBerangkat   :data.tanggalBerangkat,
            jamBerangkat       :data.jamBerangkat,
            hargaTiket         :data.hargaTiket,
            gambar             :gambar
        })

        await penumpang.findOne({kodePenumpang: data.kodePenumpang})
            .then(penumpang => {
                if (penumpang){
                    reject(response.commonErrorMsg('Kode Buku Sudah Digunakan'))
                }else {
                    penumpangBaru.save()
                        .then(r=>{
                            resolve(response.commonSuccessMsg('Input Data Berhasil'))
                        }).catch(err =>{
                        reject(response.commonErrorMsg('Input Data Gagal'))
                    })
                }
            }).catch(err => {
            reject(response.commonErrorMsg('Terjadi Kesalahan Pada Server'))
        })
    })

exports.lihatDataPenumpang = () =>
    new Promise(async (resolve, reject) => {
        await penumpang.find({})
            .then(result => {
                resolve(response.commonResult(result))
            })
            .catch(()=>reject(response.commonErrorMsg('Terjadi Kesalahan Pada Server')))
    })

exports.lihatDetailDataPenumpang = (kodePenumpang) =>
    new Promise(async (resolve, reject) => {
        await penumpang.findOne({kodePenumpang: kodePenumpang})
            .then(result => {
                resolve(response.commonResult(result))
            })
            .catch(()=>reject(response.commonErrorMsg('Terjadi Kesalahan Pada Server')))
    })

exports.updatePenumpang = (_id, data, gambar) =>
    new Promise(async (resolve, reject)=>{
        penumpang.updateOne(
            {_id : ObjectId(_id)},
            {
                $set: {
                    kodePenumpang   :data.kodePenumpang,
                    namaPenumpang   :data.namaPenumpang,
                    umur            :data.umur,
                    tujuan          :data.tujuan,
                    tanggalBerangkat:data.tanggalBerangkat,
                    jamBerangkat    :data.jamBerangkat,
                    hargaTiket      :data.hargaTiket,
                    gambar          :gambar
                }
            }
        ).then(penumpang => {
            resolve(response.commonSuccessMsg('Berhasil Mengubah Data'))
        }).catch(err => {
            reject(response.commonErrorMsg('Terjadi Kesalahan Pada Server'))
        })
    })

exports.hapusPenumpang = (_id) =>
    new Promise(async (resolve, reject) => {
        await penumpang.remove({_id: ObjectId(_id)})
            .then(() =>{
                resolve(response.commonSuccessMsg('Data Dihapus'))
            }).catch(() => {
                reject(response.commonErrorMsg('Terjadi Kesalahan Pada Server'))
            })
    })
