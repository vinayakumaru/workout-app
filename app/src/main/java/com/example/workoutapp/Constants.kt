package com.example.workoutapp

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        var  exercise = ExerciseModel(1, "Jumping Jacks", R.drawable.ic_jumping_jacks)
        exerciseList.add(exercise)

        exercise = ExerciseModel(2, "Wall Sit", R.drawable.ic_wall_sit)
        exerciseList.add(exercise)

        exercise = ExerciseModel(3, "Push Up", R.drawable.ic_push_up)
        exerciseList.add(exercise)

        exercise = ExerciseModel(4, "Step-Up onto Chair", R.drawable.ic_step_up_onto_chair)
        exerciseList.add(exercise)

        exercise = ExerciseModel(5, "Squat", R.drawable.ic_squat)
        exerciseList.add(exercise)

        exercise = ExerciseModel(6, "Triceps Dip On Chair", R.drawable.ic_triceps_dip_on_chair)
        exerciseList.add(exercise)

        exercise = ExerciseModel(7, "High Knees Running In Place", R.drawable.ic_high_knees_running_in_place)
        exerciseList.add(exercise)

        exercise = ExerciseModel(8, "Abdominal Crunch", R.drawable.ic_abdominal_crunch)
        exerciseList.add(exercise)

        exercise = ExerciseModel(9, "Plank", R.drawable.ic_plank)
        exerciseList.add(exercise)

        exercise = ExerciseModel(10, "Lunges", R.drawable.ic_lunge)
        exerciseList.add(exercise)

        exercise = ExerciseModel(11, "Push up and Rotation", R.drawable.ic_push_up_and_rotation)
        exerciseList.add(exercise)

        exercise = ExerciseModel(12, "Side Plank", R.drawable.ic_side_plank)
        exerciseList.add(exercise)

        return exerciseList
    }
}