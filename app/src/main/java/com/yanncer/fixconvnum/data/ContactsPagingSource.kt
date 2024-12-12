package com.yanncer.fixconvnum.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.repository.ContactsRepository

class ContactsPagingSource(
    private val context: Context,
    private val repository: ContactsRepository
) : PagingSource<Int, Contact>() {

    override fun getRefreshKey(state: PagingState<Int, Contact>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contact> {
        return try {
            val page = params.key ?: 0
            val contacts = repository.fetchContacts(
                limit = params.loadSize,
                offset = page * params.loadSize,
                searchQuery = ""
            )

            LoadResult.Page(
                data = contacts,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (contacts.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}