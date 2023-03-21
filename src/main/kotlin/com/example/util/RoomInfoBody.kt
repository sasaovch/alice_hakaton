package com.example.util

data class RoomInfoBody(
    val p_request: String,
    val p_flow_id: String,
    val p_flow_step_id: String,
    val p_instance: String,
    val p_arg_names: List<String>,
    val p_arg_values : List<String>
    )
