package cz.cvut.fukalhan.main.useractivity.viewModel

import cz.cvut.fukalhan.repository.entity.RunRecord

class RecordsComparator : Comparator<RunRecord> {
    override fun compare(record1: RunRecord?, record2: RunRecord?): Int {
        return when {
            record1?.date == record2?.date -> 0
            record1?.date!! < record2?.date!! -> 1
            else -> -1
        }
    }
}