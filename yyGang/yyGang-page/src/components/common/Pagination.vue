<template>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <li class="page-item">
                <a class="page-link no-active" href="#" aria-label="Previous"
                    @click.prevent="changePage(pageInfo.currentPage - 1);">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>

            <li v-for="page in generateSequence" :key="page" class="page-item">
                <a class="page-link" :class="{active: pageInfo.currentPage === page}" href="#"
                    @click.prevent="changePage(page);">
                    {{ page }}
                </a>
            </li>

            <li class="page-item">
                <a class="page-link no-active" href="#" aria-label="Next"
                    @click.prevent="changePage(pageInfo.currentPage + 1);">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
</template>

<script setup>
import { computed } from 'vue';

    const props = defineProps({
        pageInfo: {
            type: Object,
            required: true
        }
    });

    const totalPages = computed(
        () => Math.ceil(props.pageInfo.totalElements / props.pageInfo.listLimit)
    );

    const startPage = computed(
        () => (props.pageInfo.pageLimit * Math.floor((props.pageInfo.currentPage - 1) / props.pageInfo.pageLimit)) + 1
    );

    const endPage = computed(
        () => {
            let end = startPage.value + props.pageInfo.pageLimit - 1;

            return end > totalPages.value ? totalPages.value : end;
        }
    );

    const generateSequence = computed(
        () => {
            const sequence = [];

            for (let i = startPage.value; i <= endPage.value; i++) {
                sequence.push(i);
            }

            return sequence;
        }
    );

    const emit = defineEmits(['change-page']);

    const changePage = (page) => {
        emit('change-page', { page, totalPages: totalPages.value });
    };
</script>

<style scoped>
.pagination .page-item .page-link {
    color: #198754;
    border-color: #198754;
}

.pagination .page-item .page-link:hover,
.pagination .page-item .page-link:focus {
    background-color: #198754;
    color: white;
    border-color: #198754;
}

.pagination .page-item .page-link.active {
    background-color: #198754;
    border-color: #198754;
    color: white;
}

.pagination .page-item .no-active:hover,
.pagination .page-item .no-active:focus {
    background-color: transparent;
    color: #198754;
    border-color: #198754;
}

.pagination .page-link:focus {
    outline: none;
    box-shadow: none;
}
</style>
