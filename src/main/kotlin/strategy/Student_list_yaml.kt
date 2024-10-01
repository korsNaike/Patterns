package org.korsnaike.strategy

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.korsnaike.student.Student
import java.io.File

class Student_list_yaml(private var students: MutableList<Student> = mutableListOf()) {

}