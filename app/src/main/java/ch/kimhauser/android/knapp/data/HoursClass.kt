package ch.kimhauser.android.knapp.data

data class HoursClass(
    val id_hour:Int,
    val id_place:Int,
    val wd:String,
    val start_hour:Int,
    val start_minute:Int,
    val end_hour:Int,
    val end_minute:Int,
    val active:Int,
    val dt_create:String,
    val place:String,
    val description:String,
    val address:String
) {
}

//data class KnAClass(val id_place:Int,
//                    val place:String,
//                    val description:String,
//                    val address:String,
//                    val active:Int,
//                    val dt_create:String) {
//
//}