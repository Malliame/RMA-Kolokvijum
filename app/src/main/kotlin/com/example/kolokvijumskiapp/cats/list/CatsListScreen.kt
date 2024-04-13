package com.example.kolokvijumskiapp.cats.list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.kolokvijumskiapp.R
import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.list.CatsListContract.CatsListState
import com.example.kolokvijumskiapp.cats.list.CatsListContract.CatsListUiEvent
import com.example.kolokvijumskiapp.cats.list.model.CatsUiModel

fun NavGraphBuilder.cats(
    route: String,
    onUserClick: (String) -> Unit
) = composable(
    route = route
){

    val catsListViewModel = viewModel<CatsListViewModel>()

    val state = catsListViewModel.state.collectAsState()

    CatsListScreen(
        state = state.value,
        eventPublisher = {
            catsListViewModel.setEvent(it)
        },
        onUserClick = onUserClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsListScreen(
    state: CatsListState,
    eventPublisher: (CatsListUiEvent) -> Unit,
    onUserClick: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    Scaffold(
        topBar = {
            Column {

                CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.cat_breed)) })
                TextField(
                    value = text,
                    onValueChange ={
                        text=it
                        eventPublisher(CatsListUiEvent.SearchQueryChanged(it))
                    },
                    Modifier.fillMaxWidth(),
                    leadingIcon = {Icon(Icons.Default.Search,"")}

                )
            }
        },
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(

                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize().padding(top = 10.dp),
                ) {
                    items(
                        items = state.cats,
                        key = { it.id },
                    ) { cat ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 10.dp)
                                .clickable { onUserClick(cat.id) },
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 4.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = MaterialTheme.typography.headlineSmall,
                                    text = cat.name,
                                )
                                if(cat.alt_names != "") {
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        text = "(" + cat.alt_names + ")",
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                if(cat.description.length > 150){
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        text = cat.description.substring(0,147) + "..."
                                    )
                                }
                                else {
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text = cat.description
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                val traitsList = cat.temperament.split(", ")

                                Row(
                                    modifier = Modifier.padding(start = 10.dp),
                                ) {
                                    if(traitsList.size < 3){
                                        for(trait in traitsList){
                                            SuggestionChip(
                                                modifier = Modifier
                                                    .padding(3.dp)
                                                    .wrapContentWidth()
                                                    .height(20.dp),
                                                onClick = { /*TODO*/ },
                                                label = { Text(text = trait) })
                                        }
                                    }
                                    else{
                                        var counter:Int = 0
                                        for(trait in traitsList){
                                            SuggestionChip(
                                                modifier = Modifier
                                                    .padding(3.dp)
                                                    .wrapContentWidth()
                                                    .height(20.dp),
                                                onClick = { /*TODO*/ },
                                                label = { Text(text = trait) })
                                            counter++
                                            if(counter == 3) break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

class CatListStateParameterProvider : PreviewParameterProvider<CatsListState> {
    override val values: Sequence<CatsListState> = sequenceOf(
        CatsListState(
            loading = true,
        ),
        CatsListState(
            loading = false,
        ),
        CatsListState(
            loading = false,
            cats = listOf(
                CatsUiModel(id = "sphy",
                    name = "Sphynx",
                    temperament = "Loyal, Inquisitive, Friendly, Quiet, Gentle",
                    description = "The Sphynx is an intelligent, inquisitive, extremely friendly people-oriented breed. Sphynx commonly greet their owners  at the front door, with obvious excitement and happiness. She has an unexpected sense of humor that is often at odds with her dour expression.",
                    alt_names = "Canadian Hairless, Canadian Sphynx"),
                CatsUiModel(id = "soma",
                    name = "Somali",
                    temperament = "Mischievous, Tenacious, Intelligent, Affectionate, Gentle, Interactive, Loyal",
                    description = "The Somali lives life to the fullest. He climbs higher, jumps farther, plays harder. Nothing escapes the notice of this highly intelligent and inquisitive cat. Somalis love the company of humans and other animals.",
                    alt_names = "Fox Cat, Long-Haired Abyssinian"),
                CatsUiModel(id = "snow",
                    name = "Snowshoe",
                    temperament = "Affectionate, Social, Intelligent, Sweet-tempered",
                    description = "The Snowshoe is a vibrant, energetic, affectionate and intelligent cat. They love being around people which makes them ideal for families, and becomes unhappy when left alone for long periods of time. Usually attaching themselves to one person, they do whatever they can to get your attention.",
                    alt_names = "")
            ),
        ),
    )
}

@Preview
@Composable
private fun PreviewUserList(
   @PreviewParameter(CatListStateParameterProvider::class) catsListState: CatsListState,
) {
    CatsListScreen(
        state = catsListState,
        eventPublisher = {

        },
        onUserClick = {},
    )
}