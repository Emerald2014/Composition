package ru.kudesnik.composition.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.kudesnik.composition.R
import ru.kudesnik.composition.databinding.FragmentGameBinding
import ru.kudesnik.composition.domain.entity.GameResult
import ru.kudesnik.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            with(binding) {
                add(tvOption1)
                add(tvOption2)
                add(tvOption3)
                add(tvOption4)
                add(tvOption5)
                add(tvOption6)
            }
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setOnClickListenerToOptions()
        viewModel.startGame(level)
    }

    private fun observeViewModel() {
        with(binding) {
            with(viewModel) {
                question.observe(viewLifecycleOwner) {
                    tvSum.text = it.sum.toString()
                    tvLeftNumber.text = it.visibleNumber.toString()
                    for (i in 0 until tvOptions.size) {
                        tvOptions[i].text = it.options[i].toString()
                    }
                }

                percentOfRightAnswers.observe(viewLifecycleOwner) {
                    progressBar.setProgress(it, true)
                }

                enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
                    tvAnswersProgress.setTextColor(getColorByState(it))
                }

                enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                    val color = getColorByState(it)
                    progressBar.progressTintList = ColorStateList.valueOf(color)
                }

                formattedTime.observe(viewLifecycleOwner) {
                    tvTimer.text = it
                }

                minPercent.observe(viewLifecycleOwner) {
                    progressBar.secondaryProgress = it
                }

                gameResult.observe(viewLifecycleOwner) {
                    launchGameFinishedFragment(it)
                }

                progressAnswers.observe(viewLifecycleOwner) {
                    tvAnswersProgress.text = it
                }
            }
        }
    }

    private fun setOnClickListenerToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light

        }
        return ContextCompat.getColor(requireContext(), colorResId)

    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}
