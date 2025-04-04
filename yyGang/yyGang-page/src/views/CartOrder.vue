<template>
    <div>
        <CartOrderForm :cartItems="cartItems"></CartOrderForm>
    </div>
</template>

<script setup>

import CartOrderForm from '@/components/forms/CartOrderForm.vue';
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cartStores';

const cartStore = useCartStore();
const router = useRouter();

const cartItems = computed(() => cartStore.cartItems);
console.log(cartStore.cartItems);
console.log(cartItems);

</script>

<style lang="scss" scoped>

</style>