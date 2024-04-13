package com.example.kolokvijumskiapp.cats.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.kolokvijumskiapp.R
import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.api.models.image
import com.example.kolokvijumskiapp.cats.api.models.weight
import com.example.kolokvijumskiapp.cats.details.CatsDetailsContract.CatDetailsState

fun NavGraphBuilder.catDetails(
        route: String,
        arguments: List<NamedNavArgument>,
        onClose: () -> Unit,
) = composable(
        route = route,
        arguments = arguments,
){navBackStackEntry ->
        val catId = navBackStackEntry.arguments?.getString("catId")
                ?: throw IllegalStateException("catId required")

        val catDetailsViewModel = viewModel<CatDetailsViewModel>(
                factory = object : ViewModelProvider.Factory{
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return CatDetailsViewModel(catid = catId) as T
                        }
                }
        )

        val uriHandler = LocalUriHandler.current

        val state = catDetailsViewModel.state.collectAsState()

        CatDetailsScreen(
                uriHandler = uriHandler,
                state = state.value,
                onClose = onClose
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
        uriHandler: UriHandler,
        state: CatDetailsState,
        onClose: () -> Unit,
){
        val cat = state.cat;
        Scaffold(
                topBar = {
                        CenterAlignedTopAppBar(
                                title = {
                                        Column {
                                                Text(
                                                        text = cat.name,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center,
                                                )

                                                if (cat.alt_names != "") {
                                                        Text(
                                                                style = MaterialTheme.typography.titleSmall,
                                                                text = "(${cat.alt_names})",
                                                                modifier = Modifier.fillMaxWidth(),
                                                                textAlign = TextAlign.Center,
                                                        )
                                                }
                                        }
                                },
                                navigationIcon = {
                                        IconButton(onClick = { onClose.invoke() }) {
                                                Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = ""
                                                )
                                        }
                                }

                        )
                },
                content = {paddingValues ->
                        if (state.fetching) {
                                Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center,
                                ) {
                                        CircularProgressIndicator()
                                }
                        } else {
                                val scrollState = rememberScrollState()
                                Column (
                                        modifier = Modifier
                                                .padding(
                                                        top = paddingValues.calculateTopPadding() + 10.dp,
                                                        start = 10.dp,
                                                        end = 10.dp,
                                                )
                                                .verticalScroll(scrollState),

                                ){

                                        Row {
                                                if(cat.image.url != "null") {
                                                        if(state.fetchinImage){
                                                                Box(
                                                                        modifier = Modifier.size(120.dp),
                                                                        contentAlignment = Alignment.Center,
                                                                ) {
                                                                        CircularProgressIndicator()
                                                                }
                                                        }
                                                        else {
                                                                var url: String
                                                                if (cat.image.url.trim() != "") url =
                                                                        cat.image.url
                                                                else url =
                                                                        "https://i.pinimg.com/736x/2c/66/66/2c6666d2eab1956acb451ac69b06afd6.jpg"

                                                                AsyncImage(
                                                                        modifier = Modifier.size(120.dp),
                                                                        model = url,
                                                                        contentDescription = null,
                                                                )
                                                        }
                                                }
                                                Column (
                                                        modifier = Modifier.padding(start = 10.dp, top= 9.dp)
                                                ){

                                                        SuggestionChip(
                                                                modifier = Modifier
                                                                        .padding(3.dp)
                                                                        .wrapContentWidth()
                                                                        .height(20.dp),
                                                                onClick = {},
                                                                label = { Text(text = stringResource(id = R.string.origin) + ": ${cat.origin}") }
                                                        )
                                                        SuggestionChip(
                                                                modifier = Modifier
                                                                        .padding(3.dp)
                                                                        .wrapContentWidth()
                                                                        .height(20.dp),
                                                                onClick = {},
                                                                label = { Text(text = stringResource(id = R.string.weight) + ": ${cat.weight.metric} kg") }
                                                        )
                                                        SuggestionChip(
                                                                modifier = Modifier
                                                                        .padding(3.dp)
                                                                        .wrapContentWidth()
                                                                        .height(20.dp),
                                                                onClick = {},
                                                                label = { Text(text = stringResource(id = R.string.life_span) + ": ${cat.life_span}") }
                                                        )
                                                        SuggestionChip(
                                                                modifier = Modifier
                                                                        .padding(3.dp)
                                                                        .wrapContentWidth()
                                                                        .height(20.dp),
                                                                onClick = {},
                                                                label = {
                                                                        if(cat.rare == 0) Text(text = stringResource(id = R.string.not_rare_breed))
                                                                        else Text(text = stringResource(id = R.string.rare_breed))
                                                                }
                                                        )

                                                }

                                        }

                                        Divider()
                                        Spacer(modifier = Modifier.height(30.dp))
                                        Card(
                                                modifier = Modifier.fillMaxSize(),
                                                ){
                                                Column {
                                                        Text(
                                                                text = stringResource(id = R.string.description),
                                                                modifier = Modifier.padding(8.dp),
                                                                style = MaterialTheme.typography.titleSmall
                                                        )
                                                        Text(
                                                                text = "${cat.description}",
                                                                modifier = Modifier.padding(5.dp),
                                                                style = MaterialTheme.typography.labelMedium
                                                        )
                                                }

                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        if(cat.adaptability != 0){
                                                Divider()
                                                Row(
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ) {
                                                        Text(
                                                                text = stringResource(id = R.string.adaptibility) + ": ",
                                                                modifier = Modifier
                                                                .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.adaptability) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }
                                        }
                                        if(cat.affection_level != 0){
                                                Divider()
                                                Row(
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ) {
                                                        Text(
                                                                text = stringResource(id = R.string.affection_level) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.affection_level) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.child_friendly != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.child_friendly) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.child_friendly) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.energy_level != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.energy_level) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.energy_level) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.grooming != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                stringResource(id = R.string.grooming) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.grooming) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.health_issues != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                stringResource(id = R.string.health_issues) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.health_issues) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.intelligence != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                stringResource(id = R.string.intelligence) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.intelligence) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.shedding_level != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.shedding_level) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.shedding_level) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.social_needs != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.social_needs) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.social_needs) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.stranger_friendly != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.stranger_friendly) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.stranger_friendly) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        if(cat.vocalisation != 0){
                                                Divider()
                                                Row (
                                                        modifier = Modifier.padding(start = 10.dp)
                                                ){
                                                        Text(
                                                                text = stringResource(id = R.string.vocalisation) + ": ",
                                                                modifier = Modifier
                                                                        .align(Alignment.CenterVertically),
                                                                style = MaterialTheme.typography.titleMedium
                                                        )
                                                        for(i in 1..cat.vocalisation) {
                                                                SuggestionChip(
                                                                        modifier = Modifier
                                                                                .height(10.dp)
                                                                                .width(10.dp)
                                                                                .align(Alignment.CenterVertically),
                                                                        onClick = {},
                                                                        label = {},
                                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                                                containerColor = Color(0xFF069c3a)
                                                                        )
                                                                )
                                                        }
                                                }

                                        }
                                        Spacer(modifier = Modifier.height(35.dp))
                                        if(cat.wikipedia_url != "") {
                                                Button(
                                                        onClick = {
                                                        uriHandler.openUri(cat.wikipedia_url)
                                                        },
                                                        modifier = Modifier.align(Alignment.CenterHorizontally)

                                                ) {
                                                        Text(text = stringResource(id = R.string.wikipedia))
                                                }
                                        }
                                }
                        }
                }
        )
}

@Preview
@Composable
private fun PreviewUserList(
        //@PreviewParameter(CatListStateParameterProvider::class) catsListState: CatsListContract.CatsListState,
) {
        val weight: weight = weight("6 - 15", "3 - 7")
        val image: image = image("j6oFGLpRG", 768, 1024, "https://cdn2.thecatapi.com/images/j6oFGLpRG.jpg")
        val cat: CatsApiModel = CatsApiModel("char","Chartreux","Affectionate, Loyal, Intelligent, Social, Lively, Playful","France","The Chartreux is generally silent but communicative. Short play sessions, mixed with naps and meals are their perfect day. Whilst appreciating any attention you give them, they are not demanding, content instead to follow you around devotedly, sleep on your bed and snuggle with you if you’re not feeling well.","12 - 15","chao min",
                5,5,4,2,1,2,4,3,5,5,1,0,"https://en.wikipedia.org/wiki/Chartreux", image,weight,"")



        CatDetailsScreen(
                state = CatDetailsState( fetching = false, cat = cat),
                uriHandler = LocalUriHandler.current,
                onClose = {},
        )
}
