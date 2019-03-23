package com.mohamed.reactiveit.common.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mohamed.reactiveit.common.baseadapter.BaseAdapter
import com.mohamed.reactiveit.common.baseadapter.Displayable

@Entity(
    tableName = "product",
    foreignKeys = [ForeignKey(
        entity = User::class,
        childColumns = ["user_id"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )]
)
// information
// price
// rating
data class Product(
    val name: String,
    val url: String,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val price: Float,
    var inCart: Boolean = false,
    val information: String = "",
    val rating: Float = 0f,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) : Displayable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readLong())

    override fun getType(): Int {
        return BaseAdapter.STATE_START
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeLong(userId)
        parcel.writeFloat(price)
        parcel.writeByte(if (inCart) 1 else 0)
        parcel.writeString(information)
        parcel.writeFloat(rating)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}